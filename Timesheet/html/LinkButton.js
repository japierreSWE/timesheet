'use strict';

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

var LinkButton = function (_React$Component) {
	_inherits(LinkButton, _React$Component);

	function LinkButton(props) {
		_classCallCheck(this, LinkButton);

		var _this = _possibleConstructorReturn(this, (LinkButton.__proto__ || Object.getPrototypeOf(LinkButton)).call(this, props));

		_this.goTo = _this.goTo.bind(_this);
		return _this;
	}

	_createClass(LinkButton, [{
		key: "goTo",
		value: function goTo() {
			window.location = this.props.link;
		}
	}, {
		key: "render",
		value: function render() {
			return React.createElement(
				"button",
				{ onClick: this.goTo },
				this.props.text
			);
		}
	}]);

	return LinkButton;
}(React.Component);

var Buttons = function (_React$Component2) {
	_inherits(Buttons, _React$Component2);

	function Buttons() {
		_classCallCheck(this, Buttons);

		return _possibleConstructorReturn(this, (Buttons.__proto__ || Object.getPrototypeOf(Buttons)).apply(this, arguments));
	}

	_createClass(Buttons, [{
		key: "render",
		value: function render() {
			return React.createElement(
				"div",
				null,
				React.createElement(LinkButton, { text: "Create an employee account", link: "employeeCreate.html" }),
				React.createElement("br", null),
				React.createElement(LinkButton, { text: "Create an client account", link: "clientCreate.html" })
			);
		}
	}]);

	return Buttons;
}(React.Component);

ReactDOM.render(React.createElement(Buttons, null), document.getElementById("buttons"));
//