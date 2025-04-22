import { useState } from "react";
import reactLogo from "./assets/react.svg";
import viteLogo from "/vite.svg";
import "./App.css";

import {
  AuthFlowType,
  CognitoIdentityProviderClient,
  InitiateAuthCommand,
} from "@aws-sdk/client-cognito-identity-provider";

function App() {
  const [count, setCount] = useState(0);

  const [response, setResponse] = useState<any>();

  async function login() {
    const client = new CognitoIdentityProviderClient({
      region: "ap-northeast-1",
    });
    const command = new InitiateAuthCommand({
      AuthFlow: AuthFlowType.USER_PASSWORD_AUTH,
      AuthParameters: {
        USERNAME: "${emailAddress}",
        PASSWORD: "${loginPassword}",
      },
      ClientId: "${clientId}",
    });

    await client
      .send(command)
      .then((result) => {
        setResponse(result.AuthenticationResult);
      })
      .catch((error) => {
        setResponse(error);
      })
      .finally(() => {
        // finally.
      });
  }

  return (
    <>
      <div>
        <a href="https://vite.dev" target="_blank">
          <img src={viteLogo} className="logo" alt="Vite logo" />
        </a>
        <a href="https://react.dev" target="_blank">
          <img src={reactLogo} className="logo react" alt="React logo" />
        </a>
      </div>
      <h1>Vite + React</h1>
      <div className="card">
        <button onClick={() => setCount((count) => count + 1)}>
          count is {count}
        </button>
        <p>
          Edit <code>src/App.tsx</code> and save to test HMR
        </p>
      </div>
      <p className="read-the-docs">
        Click on the Vite and React logos to learn more
      </p>

      <p>
        <button onClick={login}>LOGIN</button>
        <br />
        <textarea cols={80} rows={10} value={JSON.stringify(response)} />
      </p>
    </>
  );
}

export default App;
