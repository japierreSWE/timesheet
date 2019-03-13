'use strict';
class LinkButton extends React.Component {
	
	constructor(props) {
		super(props);
		this.goTo = this.goTo.bind(this);
	}
	
	goTo() {
		window.location = this.props.link;
	}
	
	render() {
		return <button onClick={this.goTo} class="btn btn-secondary">{this.props.text}</button>;
	}
	
}

class Buttons extends React.Component {
	render() {
		return (
			<div>
			<LinkButton text="Create an employee account" link="employeeCreate.html"/><br/><br/>
			<LinkButton text="Create an client account" link="clientCreate.html"/>
			</div>
		);
	}
}

ReactDOM.render(
		<Buttons />,
		document.getElementById("buttons")
);
//