import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.tsx'
import { AuthProvider } from 'react-oidc-context'


const providerConfig = {
  authority: "https://cognito-idp.ap-northeast-1.amazonaws.com/${userPoolId}",
  client_id: "${clientId}",
  redirect_uri: `${window.location.origin}`,
  response_type: "code",
  scope: "email openid phone",
}

createRoot(document.getElementById('root')!).render(
  <AuthProvider {...providerConfig}>
  <StrictMode>
    <App />
  </StrictMode>
  </AuthProvider>
)
