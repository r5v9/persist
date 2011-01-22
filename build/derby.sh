#! /bin/sh -e

case "$1" in
  start)
    echo "Starting Derby"
	export DERBY_OPTS=-Dderby.system.home=/usr/local/derby
    start-stop-daemon --start --quiet --pidfile /var/run/derby.pid --background --exec /usr/local/derby/bin/startNetworkServer > /dev/null
    ;;
  stop)
    echo "Stopping Derby"
    /usr/local/derby/bin/stopNetworkServer
    ;;
  *)
    echo "Usage: /etc/init.d/derby {start|stop}"
    exit 1
    ;;
esac

exit 0
