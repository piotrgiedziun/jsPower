# -*- coding: utf-8 -*-
import os
import tornado.httpserver
import tornado.ioloop
import tornado.web


def main():
	application = tornado.web.Application([

		(r"/(.*?)", tornado.web.StaticFileHandler, {
			"path": os.path.join(os.path.dirname(__file__), ".")
		}),
	])

	http_server = tornado.httpserver.HTTPServer(application)
	http_server.listen(8888)
	tornado.ioloop.IOLoop.instance().start()

if __name__ == "__main__":
	main()