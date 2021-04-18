import React, {Component} from 'react'

export default class Authentication extends Component {
    constructor(props) {
        super(props);

        this.state = {
            name: "",
            surname: "",
            email: "",
            password: "",
            password_confirmation: "",
            registrationErrors: ""
        }

        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleChange = this.handleChange.bind(this);
    }

    handleSubmit(event) {
        console.log("form submitted");
        event.preventDefault();
    }

    handleChange(event) {
        console.log("handle change", event);
    }

    render() {
        return (
            <div>
                <form onSubmit={this.handleSubmit}>
                    <label for id="email">E-mail</label>
                    <input id="email" type="email" name="email" value={this.state.email} onChange={this.handleChange} required/>
                </form>
            </div>
        );
    }
}
