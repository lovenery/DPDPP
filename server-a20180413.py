import socket # https://docs.python.org/3/library/socket.html
import threading # https://docs.python.org/3/library/threading.html
import sys

# AF_INET: IPv4
# SOCK_STREAM: TCP, SOCK_DGRAM: UDP
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

addr = sys.argv[1]
port = 9999
print('addr: ' + addr + ':' + str(port))
sock.bind((addr, port))

sock.listen(1)

connections = []

def handler(c, a):
    global connections
    while True:
        data = c.recv(1024) # str
        print('get data: ' + str(data))
        for connection in connections:
            pass
            #connection.send(bytes(data))
        if not data:
            connections.remove(c)
            c.close()
            break

def input_handler(c, a):
    global connections
    while True:
        for connection in connections:
            connection.send(bytes(input("") + '\r\n', 'utf-8'))

while True:
    connection, client_address = sock.accept()
    cThread = threading.Thread(target=handler, args=(connection, client_address))
    cThread.daemon = True
    cThread.start()
    connections.append(connection)
    print(connections)

    sThread = threading.Thread(target=input_handler, args=(connection, client_address))
    sThread.daemon = True
    sThread.start()

"""
# Server can send msg to all client. Client can send msg to server.

# Server
python server-a20180413.py 127.0.0.1
# Client
telnet 127.0.0.1 9999
"""
