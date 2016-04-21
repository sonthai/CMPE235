'use strict';

module.exports = function(app) {
	// Routing logic   
	var comments = require('../../app/controllers/comments.server.controller');

	app.route('/comments/list').get(comments.list_comments);
	app.route('/comments/add').post(comments.add_comment);
};
