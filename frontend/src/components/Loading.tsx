import React, { FC } from "react";
import { Container, Spinner } from "react-bootstrap";

// Loading component with spinner to show while waiting for database requests
const Loading: FC = () => {
  return (
    <Container>
      <Spinner animation="border" variant="secondary" className="mb-3" />
      <p>Loading</p>
    </Container>
  );
};

export default Loading;
