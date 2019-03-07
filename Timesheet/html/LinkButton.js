'use strict';
class LinkButton extends React.Component {
	
	onClick() {
		window.location = this.props.link;
	}
	
	render() {
		return <button onclick={this.onClick}>{this.props.text}</button>;
	}
	
}
ReactDOM.render(
		<LinkButton text="Create an employee account" link="employeeCreate.html" />,
		document.getElementById("root")
);
//