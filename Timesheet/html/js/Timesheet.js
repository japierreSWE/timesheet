'use strict';

class Row extends React.Component {
	constructor(props) {
		super(props);
		this.startRef = React.createRef();
		this.endRef = React.createRef();
		this.timeRef = React.createRef();
		this.onTimeChange = this.onTimeChange.bind(this);
	}
	
	onTimeChange() {
		var start = this.startRef.current;
		var end = this.endRef.current;
		var time = this.timeRef.current;
		this.props.change(this.props.rowID, start.value, end.value);
		
		/*if(start.value == "" || end.value == "") { //don't do anything unless both have values
			time.textContent = "";
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
			
		}*/
	}
	
	render() {
		var id = String(this.props.rowID);
		var timeString;
		if(this.props.time == 0 || this.props.time == null || this.props.time == undefined) {
			timeString = "";
		} else if(this.props.time < 0) {
			timeString = "Error: Start time is after end time";
		} else {
			timeString = this.props.time.toFixed(2) + " hours";
		}
		
		return (
			<div id={"row"+id}>
				<p>Date:</p> <input type="date" id={"date"+id}/>
				<p> Start Time</p> <input type="time" id={"start" + id} ref={this.startRef} onChange={this.onTimeChange}/>
				<p> End Time</p> <input type="time" id={"end"+ id} ref={this.endRef} onChange={this.onTimeChange}/>
				<p> Comments:</p> <input type="text" id={"comments"+ id}/>
				<p> Time spent:</p><p id={"time" + id} ref={this.timeRef}>{timeString}</p>
			</div>
		);
	}
	
}

class Timesheet extends React.Component {
	constructor(props) {
		super(props);
		this.state = {rowIDs: [1], times:[0], total: 0};
		this.addRow = this.addRow.bind(this);
		this.deleteRow = this.deleteRow.bind(this);
		this.makeRow = this.makeRow.bind(this);
		this.handleTimesChange = this.handleTimesChange.bind(this);
		this.findTotalTime = this.findTotalTime.bind(this);
	}
	
	//generates a React Row with appropriate props
	makeRow(rowID) {
		//1st rowID is 1. so rowID-1 is the index of the row
		var index = rowID-1;
		var time = this.state.times[index];
		return <Row rowID={String(rowID)} key={String(rowID)} time={time} change={this.handleTimesChange}/>;
	}
	
	//adds a row to the end
	addRow() {
		this.setState(function(state) {
			var IDArr = this.state.rowIDs;
			var timesArr = this.state.times;
			timesArr.push(0);
			IDArr.push(IDArr.length + 1); //adds the next number to the end
			return {rowIDs: IDArr, times: timesArr};
		});
	}
	
	//deletes a row from the end
	deleteRow() {
		this.setState(function(state) {
			var IDArr = this.state.rowIDs;
			var timesArr = this.state.times;
			if(IDArr.length > 1) { //don't delete if there's only one row. timesheets should have at least one row
				IDArr.pop();
				timesArr.pop();
			}
			return {rowIDs: IDArr, times: timesArr};
		});
		this.findTotalTime();
	}
	
	//converts a time string into numbers in the form of an array
	convertTime(timeStr) {
		var arr = timeStr.split(":");
		arr[0] = parseInt(arr[0]);
		arr[1] = parseInt(arr[1]);
		return arr;
	}
	
	//find the total amount of hours spent and changes state accordingly
	findTotalTime() {
		this.setState(function(state) {
			var sum = 0;
			for(i = 0; i<state.times.length; i++) {
				if(state.times[i] != null && state.times[i] != undefined) { //ignore null and undefined values
					sum += state.times[i];
				}
			}
			return {total: sum};
		});
	}
	
	//runs when a row's times changes
	handleTimesChange(rowID, startTime, endTime) {
		var index = parseInt(rowID) - 1;
		
		if(startTime == "" || endTime == "") { //if not both fields have time values, set the row's time to 0
			this.setState(function(state) {
				state.times[index] = 0;
				return {times: state.times};
			});
		} else {
			var startArr = convertTime(startTime);
			var endArr = convertTime(endTime);
			var startMinutes = startArr[0]*60 + startArr[1];
			var endMinutes = endArr[0]*60 + endArr[1]; //convert each time into # of minutes passed
			var diff = (endMinutes - startMinutes) / 60.0; //find the difference of minutes in hours
			
			this.setState(function(state) { //set the row's time in the state
				state.times[index] = diff;
				return {times: state.times};
			});
			
			this.findTotalTime();
		}	
	}
	
	render() {
		return (
			<div id="sheet">
				<p>Total hours worked:</p><p> {this.state.total}</p>
				<div id="rows">
					{this.state.rowIDs.map(rowID => this.makeRow(rowID))}
				</div><br/>
				<button onClick={this.addRow}>Add Row</button>
				<button onClick={this.deleteRow}>Delete Row</button>
			</div>
		)
	}
}
ReactDOM.render(<Timesheet />, document.getElementById("root"));