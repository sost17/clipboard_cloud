# coding=utf-8
from wsgiref.simple_server import make_server
from wsgiref.util import setup_testing_defaults

file = 'G:\py\clipboard\cache.txt'

def application(environ,start_response):
    setup_testing_defaults(environ)
    start_response('200 OK',[('Content-Type','text/plain; charset=utf-8')])

    f = open(file,encoding = 'utf-8', mode = 'r')
    message = f.read()
    f.close()

    return [message.encode('utf-8')]

httpd = make_server('',8000,application)
httpd.serve_forever()