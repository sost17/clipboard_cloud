# coding=utf-8

import socket

sock = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
sock.bind(("locahost",2333))
sock.listen(5)

file = "/var/www/html/cache.txt"

while True:
    conn,addr = sock.accept()
    print(addr)
    msg = conn.recv(4096)
    f = open(file,'wb')
    f.write(msg)
    f.close()
    conn.close()
sock.close()