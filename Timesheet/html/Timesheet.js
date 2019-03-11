'use strict';

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

var Row = function (_React$Component) {
	_inherits(Row, _React$Component);

	function Row(props) {
		_classCallCheck(this, Row);

		var _this = _possibleConstructorReturn(this, (Row.__proto__ || Object.getPrototypeOf(Row)).call(this, props));

		_this.startRef = React.createRef();
		_this.endRef = React.createRef();
		_this.timeRef = React.createRef();
		_this.onTimeChange = _this.onTimeChange.bind(_this);
		return _this;
	}

	//converts a time string into numbers in the form of an array


	_createClass(Row, [{
		key: "convertTime",
		value: function convertTime(timeStr) {
			var arr = timeStr.split(":");
			arr[0] = parseInt(arr[0]);
			arr[1] = parseInt(arr[1]);
			return arr;
		}
	}, {
		key: "onTimeChange",
		value: function onTimeChange() {
			var start = this.startRef.current;
			var end = this.endRef.current;
			var time = this.timeRef.current;

			if (start.value == "" || end.value == "") {
				//don't do anything unless both have values
				time.textContent = "";
				return;
			} else {
				var startArr = convertTime(start.value);
				var endArr = convertTime(end.value);
				var startMinutes = startArr[0] * 60 + startArr[1];
				var endMinutes = endArr[0] * 60 + endArr[1]; //convert each time into # of minutes passed
				var diff = (endMinutes - startMinutes) / 60.0; //find the difference of minutes in hours

				if (diff < 0) {
					time.textContent = "Error: End time is before start time";
				} else {
					time.textContent = diff.toFixed(2) + " hours";
				}
			}
		}
	}, {
		key: "render",
		value: function render() {
			var id = String(this.props.rowID);
			return React.createElement(
				"div",
				{ id: "row" + id },
				React.createElement(
					"p",
					null,
					"Date:"
				),
				" ",
				React.createElement("input", { type: "date", id: "date" + id }),
				React.createElement(
					"p",
					null,
					" Start Time"
				),
				" ",
				React.createElement("input", { type: "time", id: "start" + id, ref: this.startRef, onChange: this.onTimeChange }),
				React.createElement(
					"p",
					null,
					" End Time"
				),
				" ",
				React.createElement("input", { type: "time", id: "end" + id, ref: this.endRef, onChange: this.onTimeChange }),
				React.createElement(
					"p",
					null,
					" Comments:"
				),
				" ",
				React.createElement("input", { type: "text", id: "comments" + id }),
				React.createElement(
					"p",
					null,
					" Time spent:"
				),
				React.createElement("p", { id: "time" + id, ref: this.timeRef })
			);
		}
	}]);

	return Row;
}(React.Component);

var Timesheet = function (_React$Component2) {
	_inherits(Timesheet, _React$Component2);

	function Timesheet(props) {
		_classCallCheck(this, Timesheet);

		var _this2 = _possibleConstructorReturn(this, (Timesheet.__proto__ || Object.getPrototypeOf(Timesheet)).call(this, props));

		_this2.state = { rowIDs: [1] };
		_this2.addRow = _this2.addRow.bind(_this2);
		_this2.deleteRow = _this2.deleteRow.bind(_this2);
		return _this2;
	}

	//generates a React Row with appropriate props


	_createClass(Timesheet, [{
		key: "makeRow",
		value: function makeRow(rowID) {
			return React.createElement(Row, { rowID: String(rowID), key: String(rowID) });
		}

		//adds a row to the end

	}, {
		key: "addRow",
		value: function addRow() {
			this.setState(function (state) {
				var IDArr = this.state.rowIDs;
				IDArr.push(IDArr.length + 1); //adds the next number to the end
				return { rowIDs: IDArr };
			});
		}

		//deletes a row from the end

	}, {
		key: "deleteRow",
		value: function deleteRow() {
			this.setState(function (state) {
				var IDArr = this.state.rowIDs;
				if (IDArr.length > 1) {
					//don't delete if there's only one row. timesheets should have at least one row
					IDArr.pop();
				}
				return { rowIDs: IDArr };
			});
		}
	}, {
		key: "render",
		value: function render() {
			var _this3 = this;

			return React.createElement(
				"div",
				{ id: "rows" },
				this.state.rowIDs.map(function (rowID) {
					return _this3.makeRow(rowID);
				}),
				React.createElement("br", null),
				React.createElement(
					"button",
					{ onClick: this.addRow },
					"Add Row"
				),
				React.createElement(
					"button",
					{ onClick: this.deleteRow },
					"Delete Row"
				)
			);
		}
	}]);

	return Timesheet;
}(React.Component);

ReactDOM.render(React.createElement(Timesheet, null), document.getElementById("sheet"));