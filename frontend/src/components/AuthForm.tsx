import React, { useState, useContext } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { Button, Container, Form, FormControl } from "react-bootstrap";
import { UserContext } from "../contexts/UserContext";
import { UserContextType } from "../types/UserContextType";

const AuthForm = () => {
  const { user, updateUser } = useContext(UserContext) as UserContextType;
  const [formData, setFormData] = useState({
    userId: "",
    password: "",
  });
  const [isSignup, setIsSignup] = useState(false);

  const navigate = useNavigate();

  const handleChange = (e: any) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e: any) => {
    e.preventDefault();
    if (isSignup) {
      // Handle signup
      await doSignUp();
    } else {
      // Handle signin
      await doSignIn();
    }
  };

  const doSignUp = async () => {
    try {
      const res = await axios.post("/api/auth/signUp", formData);

      console.log(res.status);

      if (res.status === 201) {
        const user = await axios.get("api/auth/user");
        console.log(user.data);
        updateUser(user.data);
        navigate("/");
      }

      console.log(res);
    } catch (e) {
      console.log(e);
    }
  };

  const doSignIn = async () => {
    try {
      const res = await axios.post("/api/auth/login", formData);

      console.log(res);

      if (res.status === 204) {
        const user = await axios.get("api/auth/user");
        console.log(user.data);
        updateUser(user.data);
        navigate("/");
      }
    } catch (e) {
      console.log(e);
    }
  };

  const switchMode = () => {
    setIsSignup((prevIsSignup) => !prevIsSignup);
    // Find a way to clear input fields
  };

  return (
    <Container>
      <h1 className="mt-4 mb-4">Authentication</h1>

      <Form>
        <Form.Text>{isSignup ? "Sign up" : "Login"}</Form.Text>
        <FormControl type={"text"} name={"userId"} onChange={handleChange} />
        <FormControl
          className="mt-2"
          type={"password"}
          name={"password"}
          onChange={handleChange}
        />
        <Button className="mt-3" onClick={handleSubmit}>
          {isSignup ? "Sign Up" : "Login"}
        </Button>
      </Form>
      <Button className="mt-3" id="switch" onClick={switchMode}>
        {isSignup
          ? "Already have an account? Sign In"
          : "Dont have an account yet? Sign Up"}
      </Button>
    </Container>
  );
};

export default AuthForm;
