PID_FILE="server.pid"

sudo docker compose up --build -d
cd website
python -m http.server 8000 > server.log 2>&1 & echo $! > $PID_FILE
cd ..