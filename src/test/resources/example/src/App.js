import React, { Component } from 'react';
import './App.css';

class App extends Component {

  constructor() {
	  super();
	  this.state = {
	     content:''
	  }
  }	
	
  componentDidMount() {
	
	var url = "data/en.json";  
	  
	var location = window.location.search;
	if (location.indexOf("lang=en") <= 0) {
		url = "data/" + location.substring(location.indexOf("=")+1) + ".json";
	}//end if
	
	console.log(url);
	  
	fetch(url)
		.then(response => {return response.json()})
		.then(data => {
			this.setState({content:data}) 
		});	  
  }	
	
  render() {
	

	  
    return (
      <div className="App">
        <p className="App-intro">
          <input type="text" name="someInputField" placeholder={this.state.content.placeholder} className="inputField"/>
        </p>
      </div>
    );
  }
}

export default App;
