'use strict';

class Row extends React.Component {
	constructor(props) {
		super(props);
		this.startRef = React.createRef();
		this.endRef = React.createRef();
		this.timeRef = React.createRef();
		this.onTimeChange = this.onTimeChange.bind(this);
	}
	
	//converts a time string into numbers in the form of an array
	convertTime(timeStr) {
		var arr = timeStr.split(":");
		arr[0] = parseInt(arr[0]);
		arr[1] = parseInt(arr[1]);
		return arr;
	}
	
	onTimeChange() {
		var start = this.startRef.current;
		var end = this.endRef.current;
		var time = this.timeRef.current;
		
		if(start.value == "" || end.value == "") { //don't do anything unless both have values
			return;
		} else {	
			var startArr = convertTime(start.value);
			var endArr = convertTime(end.value);
			var startMinutes = startArr[0]*60 + startArr[1];
			var endMinutes = endArr[0]*60 + endArr[1]; //convert each time into # of minutes passed
			var diff = (endMinutes - startMinutes) / 60.0; //find the difference of minutes in hours
			
			if(diff < 0) {
				time.textContent = "Error: End time is before start time";
			} else {
				time.textContent = diff.toFixed(2) + " hours";
			}
			
		}
	}
	
	render() {
		var id = String(this.props.rowID);
		return (
			<div id={"row"+id}>
				<p>Date:</p> <input type="date" id={"date"+id}/>
				<p> Start Time</p> <input type="time" id={"start" + id} ref={this.startRef} onChange={this.onTimeChange}/>
				<p> End Time</p> <input type="time" id={"end"+ id} ref={this.endRef} onChange={this.onTimeChange}/>
				<p> Comments:</p> <input type="text" id={"comments"+ id}/>
				<p> Time spent:</p><p id={"time" + id} ref={this.timeRef}/>
			</div>
		);
	}
	
}

class Timesheet extends React.Component {
	constructor(props) {
		super(props);
		this.state = {rowIDs: [1]};
	}
	
	makeRow(rowID) {
		return <Row rowID={String(rowID)} key={String(rowID)} />;
	}
	
	render() {
		return (
			<div id="rows">
			{this.state.rowIDs.map(rowID => this.makeRow(rowID))}
			</div>
		)
	}
}
ReactDOM.render(<Timesheet />, document.getElementById("sheet"));