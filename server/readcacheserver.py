# coding=utf-8

import socket

sock = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
sock.bind(("locahost",3222))
sock.listen(5)

file = "/var/www/html/cache.txt"

while True:
    conn,addr = sock.accept()
    print(addr)
    f = open(file,encoding='utf-8',mode='r')
    msg = f.read()
    f.close()
    conn.send(msg.encode('utf-8'))
    conn.close()
sock.close()