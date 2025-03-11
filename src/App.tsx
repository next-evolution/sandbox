
import "./App.css";
import { useAuth } from "react-oidc-context";

function App() {

  const auth = useAuth();

  const login = () => {
    auth.signinRedirect();
  }

  const logout = () => {
    auth.signoutSilent();
    sessionStorage.clear()
  }

  return (
    <>
      <h3>cognito react</h3>
      <hr />

      {!auth.isLoading && auth.isAuthenticated ? (
        <>
          <div>welcome</div>
          <div>
            <textarea rows={10} cols={120}>{auth.user?.access_token}</textarea>
          </div>
          <div>
            <textarea rows={10} cols={120}>{auth.user?.id_token}</textarea>
          </div>
          <div>
            <input type="button" value="LOGOUT" onClick={logout} />
          </div>
        </>
      ) : (
        <div><input type="button" value={"LOGIN"} onClick={login} /></div>
      )}
    </>
  );
}

export default App;
