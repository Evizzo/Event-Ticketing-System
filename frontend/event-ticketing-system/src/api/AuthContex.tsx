import { ReactNode, createContext, useContext, useState } from "react";
import { apiClient } from "../api/ApiClient";
import { executeJwtAuthenticationService, executeLogout, executeRegistration } from "./ApiService";

export const AuthContext = createContext({
    isAuthenticated: false,
    login: async (email: string, password: string) => false,
    register: async (userData: any) => false,
    logout: () => {},
    email: '',
    token: '',
  })
  
export const useAuth = () => useContext(AuthContext)

export default function AuthProvider({ children }: { children: ReactNode }) {

    const [isAuthenticated, setAuthenticated] = useState(false)
    const [email, setEmail] = useState("")
    const [token, setToken] = useState("")

    async function login(email: string, password: string) {
        try {
            const response = await executeJwtAuthenticationService(email, password)
            console.log(response.status)
            if(response.status === 200){
                console.log("resp stat is 200")
                const jwtToken = 'Bearer ' + response.data.token
                console.log(jwtToken)
                
                setAuthenticated(true)
                setEmail(email)
                setToken(jwtToken)

                apiClient.interceptors.request.use(
                    (config) => {
                        console.log('intercepting and adding a token')
                        config.headers.Authorization = jwtToken
                        return config
                    }
                )

                return true            
            } else {
                console.log("failed at authcontext 47")
                logout()
                return false
            }    
        } catch(error) {
            console.error(error)
            logout()
            return false
        }
    }

    async function register(userData: any) {
      try {
          const response = await executeRegistration(userData)
          console.log(response.status)
          if(response.status === 200){
              console.log("resp stat is 200 for register")
              const jwtToken = 'Bearer ' + response.data.token
              console.log(jwtToken)
              
              setAuthenticated(true)
              setEmail(userData.email)
              setToken(jwtToken)

              apiClient.interceptors.request.use(
                  (config) => {
                      console.log('intercepting and adding a token')
                      config.headers.Authorization = jwtToken
                      return config
                  }
              )

              return true            
          } else {
              console.log("failed at authcontext 79")
              logout()
              return false
          }    
      } catch(error: any) {
            if (error.response && error.response.data) {
                alert(error.response.data.message)
            }
            else{
                alert(`Error during registration: ${error}`);
            }

            logout()
            return false
      }
  }
    function logout() {
        async function performLogout() {
          try {
            const response = await executeLogout();
            console.log(response.status)
            if (response.status === 200) {
              setAuthenticated(false);
              setToken('');
              setEmail('');

              window.location.reload()

              console.log('Logout successful');
            } else {
              console.error('Logout failed');
            }
          } catch (error) {
            console.error('Error during logout:', error);
          }
        }
      
        performLogout();
      }

    return (
        <AuthContext.Provider value={ {isAuthenticated, login, register, logout, email, token }  }>
            {children}
        </AuthContext.Provider>
    )
} 