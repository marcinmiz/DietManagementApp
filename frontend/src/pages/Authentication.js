import React, {Component} from 'react'
import "../Authentication.css"

export default class Authentication extends Component {
    constructor(props) {
        super(props);

        this.state = {
            name: "",
            surname: "",
            email: "",
            password: "",
            password_confirmation: "",
            registrationErrors: "",
            mode: "login"
        }

        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleChange = this.handleChange.bind(this);
        this.handleTab = this.handleTab.bind(this);
    }

    handleSubmit(event) {
        console.log("form submitted");
        event.preventDefault();
    }

    handleChange(event) {
        this.setState({
            [event.target.name]: event.target.value
        });
    }

    handleTab(event) {
        if (event.target.name === "register_tab") {
            this.setState({mode: "register"})
        } else {
            this.setState({mode: "login"})
        }
    }

    render() {
        if (this.state.mode === "register") {
            return <div className="container">
                <div className="card auth_card">
                    <div className="logo">
                        DIETIX
                    </div>
                    <div className="tabs_area">
                        <button
                            type="button"
                            className="tab first_tab"
                            name="login_tab"
                            onClick={this.handleTab}>
                            Log in
                        </button>
                        <button
                            type="button"
                            className="tab last_tab current_tab"
                            name="register_tab"
                            onClick={this.handleTab}>
                            Register
                        </button>
                    </div>
                    <form className="form" onSubmit={this.handleSubmit}>

                        <label htmlFor="name">Name</label>
                        <input
                            className="input_field"
                            id="name"
                            type="text"
                            name="name"
                            value={this.state.name}
                            onChange={this.handleChange}
                            required/>

                        <label htmlFor="surname">Surname</label>
                        <input
                            className="input_field"
                            id="surname"
                            type="text"
                            name="surname"
                            value={this.state.surname}
                            onChange={this.handleChange}
                            required/>

                        <label htmlFor="email">E-mail</label>
                        <input
                            className="input_field"
                            id="email"
                            type="email"
                            name="email"
                            value={this.state.email}
                            onChange={this.handleChange}
                            required/>

                        <label htmlFor="password">Password</label>
                        <input
                            className="input_field"
                            id="password"
                            type="password"
                            name="password"
                            value={this.state.password}
                            onChange={this.handleChange}
                            required/>

                        <label htmlFor="passwordConfirmation">Password Confirmation</label>
                        <input
                            className="input_field"
                            id="passwordConfirmation"
                            type="password"
                            name="passwordConfirmation"
                            value={this.state.passwordConfirmation}
                            onChange={this.handleChange}
                            required/>

                        <button className="form_button" type="submit">Register</button>

                    </form>
                </div>
            </div>

        } else {
          return <div className="container">
              <div className="card auth_card">
                  <div className="logo">
                      DIETIX
                  </div>
                  <div className="tabs_area">
                      <button
                          type="button"
                          className="tab first_tab current_tab"
                          name="login_tab"
                          onClick={this.handleTab}>
                          Log in
                      </button>
                      <button
                          type="button"
                          className="tab last_tab"
                          name="register_tab"
                          onClick={this.handleTab}>
                          Register
                      </button>
                  </div>
                  <form className="form" onSubmit={this.handleSubmit}>

                      <label htmlFor="email">E-mail</label>
                      <input
                          className="input_field"
                          id="email"
                          type="email"
                          name="email"
                          value={this.state.email}
                          onChange={this.handleChange}
                          required/>

                      <label htmlFor="password">Password</label>
                      <input
                          className="input_field"
                          id="password"
                          type="password"
                          name="password"
                          value={this.state.password}
                          onChange={this.handleChange}
                          required/>

                      <button className="form_button" type="submit">Log in</button>

                  </form>
              </div>
          </div>
        }
    }
}
