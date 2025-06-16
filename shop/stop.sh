PID_FILE="server.pid"

cd website

if [ -f "$PID_FILE" ]; then
    kill $(cat $PID_FILE)
    rm $PID_FILE
fi

sudo docker compose down

cd ..
