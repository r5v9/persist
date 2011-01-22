#!/usr/bin/env python

import commands
from fabric.api import local, run, put, get, sudo, env, require

env.host_string = 'ubuntu@dbvm'

def install_mysql():
	print 'installing mysql'
	_remove_package('mysql-server')
	sudo('rm -rf /etc/mysql && rm -rf /var/lib/mysql')
	_install_package('mysql-server')
	sudo("""perl -pi -e "s/bind-address\s+=\s+127.0.0.1/bind-address = 0.0.0.0/g" /etc/mysql/my.cnf""")
	sudo('/etc/init.d/mysql restart')
	put('../src/tests/net/sf/persist/tests/mysql/mysql.sql', '~')
	run('mysql -u root < ~/mysql.sql')

def install_postgresql():
	print 'installing postgres'
	_remove_package('postgresql')
	sudo('rm -rf /etc/postgresql && rm -rf /var/lib/postgresql')
	_install_package('postgresql')
	sudo("""perl -pi -e "s|127.0.0.1/32|0.0.0.0/0|g" /etc/postgresql/8.4/main/pg_hba.conf""")
	sudo("""perl -pi -e "s/local.*all.*postgres.*ident/local all postgres ident\nlocal all persist trust/g" /etc/postgresql/8.4/main/pg_hba.conf""")
	sudo("""perl -pi -e "s/#listen_addresses = 'localhost'/listen_addresses = '*'/g" /etc/postgresql/8.4/main/postgresql.conf""")
	sudo('/etc/init.d/postgresql restart')
	put('../src/tests/net/sf/persist/tests/postgresql/postgresql-user.sql', '~')
	put('../src/tests/net/sf/persist/tests/postgresql/postgresql.sql', '~')
	run('sudo -u postgres psql < ~/postgresql-user.sql')
	run('psql -d persist -U persist < ~/postgresql.sql')

def install_oracle():
	print 'installing oracle'
	run('wget -c http://oss.oracle.com/debian/dists/unstable/main/binary-i386/libaio_0.3.104-1_i386.deb')
	run('wget -c http://oss.oracle.com/debian/dists/unstable/non-free/binary-i386/oracle-xe-universal_10.2.0.1-1.1_i386.deb')
	_remove_deb('oracle-xe-universal')
	_remove_deb('libaio')
	sudo('rm -rf /usr/lib/oracle')
	_install_package('libc6-i386 bc')
	_install_deb('libaio_0.3.104-1_i386.deb')
	_install_deb('oracle-xe-universal_10.2.0.1-1.1_i386.deb')
	sudo("""echo -e "8080\n1521\nroot\nroot\ny\n" | /etc/init.d/oracle-xe configure""")
	run("""if ! grep -q "ORACLE_HOME" ~/.profile 2>/dev/null; then echo -e '\nexport ORACLE_HOME=/usr/lib/oracle/xe/app/oracle/product/10.2.0/server\nexport PATH=$PATH:$ORACLE_HOME/bin\nexport ORACLE_SID=XE' >> ~/.profile; fi""")
	run('echo "EXEC DBMS_XDB.SETLISTENERLOCALACCESS(FALSE);" | sqlplus -S system/root')
	put('../src/tests/net/sf/persist/tests/oracle/oracle-user.sql', '~')
	run('sqlplus SYS/root as SYSDBA < ~/oracle-user.sql')	
	put('../src/tests/net/sf/persist/tests/oracle/oracle.sql', '~')
	run('sqlplus persist/persist < ~/oracle.sql')

def install_derby():
	print 'installing derby'
	_install_package('openjdk-6-jre-headless')
	run('wget -c http://apache.mirror.aussiehq.net.au//db/derby/db-derby-10.7.1.1/db-derby-10.7.1.1-bin.tar.gz')
	sudo('cd /usr/local && rm -rf derby* && tar xfz ~/db-derby*.tar.gz && ln -s db-derby* derby')
	sudo("""perl -pi -e "s/NetworkServerControl start/NetworkServerControl start -h 0.0.0.0/g" /usr/local/derby/bin/startNetworkServer""")
	put('derby.sh', '/tmp')
	sudo('rm -f /etc/init.d/derby && mv /tmp/derby.sh /etc/init.d/derby && chmod a+x /etc/init.d/derby && update-rc.d derby defaults')
	sudo('/etc/init.d/derby start')
	put('../src/tests/net/sf/persist/tests/derby/derby.sql', '~')
	sudo('sleep 1 && cd /usr/local/derby && bin/ij < ~/derby.sql')
	
def install_h2():
	print 'installing h2'
	_install_package('openjdk-6-jre-headless')
	run('wget -c http://www.h2database.com/h2-2010-11-21.zip')
	sudo('cd /usr/local && rm -rf h2* && unzip ~/h2*zip')
	sudo("""echo -e "#!/bin/sh\njava -cp /usr/local/h2/bin/h2-1.2.147.jar org.h2.tools.Server -web -webAllowOthers -tcp -tcpAllowOthers -baseDir /usr/local/h2" > /usr/local/h2/bin/h2-server && chmod a+x /usr/local/h2/bin/h2-server""")
	put('h2.sh', '/tmp')
	sudo('rm -f /etc/init.d/h2 && mv /tmp/h2.sh /etc/init.d/h2 && chmod a+x /etc/init.d/h2 && update-rc.d h2 defaults')
	sudo('/etc/init.d/h2 start')
	put('../src/tests/net/sf/persist/tests/h2/h2.sql', '~')
	run('java -cp /usr/local/h2/bin/h2-1.2.147.jar org.h2.tools.Shell -url jdbc:h2:tcp://127.0.0.1/persist -user persist -password persist < ~/h2.sql')	
	
def install():
	install_mysql()
	install_postgresql()
	install_oracle()
	install_derby()
	install_h2()

# -------------------- helpers --------------------

def _is_package_installed(package_name):
	output = run('dpkg -l %s' % package_name)
	return 'ii  %s' % package_name in output

def _remove_package(package_name):
	if _is_package_installed(package_name):
		sudo('bash -c "export DEBIAN_FRONTEND=noninteractive && apt-get remove --purge -y --assume-yes %s && apt-get autoremove --purge -y --assume-yes"' % package_name)
	
def _install_package(package_name):
	sudo('bash -c "export DEBIAN_FRONTEND=noninteractive && apt-get -y --assume-yes install %s"' % package_name)

def _remove_deb(package_name):
	if _is_package_installed(package_name):
		sudo('bash -c "export DEBIAN_FRONTEND=noninteractive && dpkg --purge %s && apt-get autoremove --purge -y --assume-yes"' % package_name)

def _install_deb(deb_file):
	sudo('bash -c "export DEBIAN_FRONTEND=noninteractive && dpkg -i --force-architecture %s"' % deb_file)
