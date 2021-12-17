import React, { FC, useState } from "react";
import { Alert, Button, Modal } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import { IResponse } from "../interfaces/IResponse";

// Component for giving response to user based on status-code from http-request
const ResponseView: FC<IResponse> = ({ message, statusCode }) => {
  const [isPopup, setIsPopup] = useState<boolean>(true);
  let responseMessage;
  let color;

  const navigate = useNavigate();

  // Set response message and color based on status-code category
  if (statusCode >= 200 && statusCode < 300) {
    responseMessage = "Success";
    color = "success";
  } else if (statusCode >= 400) {
    responseMessage = "Failed";
    color = "danger";
  } else if (statusCode === 0) {
    responseMessage = "No response";
    color = "danger";
  } else {
    responseMessage = "Unknown response";
    color = "warning";
  }

  const handleClose = () => setIsPopup(false);
  const handleShow = () => setIsPopup(true);

  return (
    <Modal show={isPopup} onHide={handleClose}>
      <Modal.Header closeButton onClick={handleClose} />
      <Modal.Body>
        <Alert variant={color}>
          <Alert.Heading>{responseMessage}</Alert.Heading>
          <p>{message}</p>
          <Button onClick={() => navigate("/")}>Go to home page</Button>
        </Alert>
      </Modal.Body>
    </Modal>
  );
};

export default ResponseView;
