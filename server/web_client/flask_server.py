# coding=utf-8
from flask import Flask
from flask import request

file = "/var/www/html/cache.txt"

app = Flask(__name__)

@app.route('/',methods=['GET','POST'])
def read():
    f = open(file, encoding='utf-8', mode='r')
    message = f.read()
    f.close()

    return message.encode('utf-8')

@app.route('/write',methods=['GET'])
def write_from():
    return '''<form action="/write" method="post">
              <p><h1>请输入信息！</h1></p>
              <p><textarea name="message"></textarea></p>
              <p><button type="submit">submit</button></p>
              </form>'''

@app.route('/write',methods=['POST'])
def write():
    message = request.form['message']
    if message == "":
        return '''<form action="/write" method="get">
              <p><h1>message is null!!</h1></p>
              <p><button type="submit">write</button></p>
              </form>'''

    f = open(file, 'wb')
    f.write(message.encode('utf-8'))
    f.close()

    return b'<h1>Successfull!!</h1>'

if __name__ == '__main__':
    app.run()
