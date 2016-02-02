var React = require('react');
var Space = require('./Space.js');

var Workspace = React.createClass({
	render: function() {
		return <div className="container-fluid workspace">
			{this.props.spaces.map(function (space) {
				return <Space name={space.name} people={space.people}/>;
			})}
      	</div>;
	}
});

module.exports = Workspace;
