#! /bin/sh -e

case "$1" in
  start)
    echo "Starting H2"
    start-stop-daemon --start --quiet --pidfile /var/run/h2.pid --background --exec /usr/local/h2/bin/h2-server > /dev/null
    ;;
  stop)
    echo "Stopping H2"
    kill `ps auxwww | grep h2 | grep java | awk '{print $2}'`
    ;;
  *)
    echo "Usage: /etc/init.d/h2 {start|stop}"
    exit 1
    ;;
esac

exit 0