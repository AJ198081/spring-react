import React, {useState} from "react";
import {LoginDetails} from "../types/LoginDetails.ts";
import {login} from "../services/AuthorisationService.ts";

const LoginComponent = () => {

    const [username, setUsername]: [string, React.Dispatch<React.SetStateAction<string>>] = useState<string>('');
    const [password, setPassword]: [string, React.Dispatch<React.SetStateAction<string>>] = useState<string>('');

    const handleLogin = (e: React.MouseEvent<HTMLButtonElement>) => {
        e.preventDefault();

        const loginObject : LoginDetails = {usernameOrEmail: username, password} as LoginDetails;

        (async () => {
            try {
                const response = await login(loginObject);
                if (response.status === 204) {
                    console.log(response);
                }
            } catch (error){
                console.log(error);
            }
        })();
    };
    return (
        <div className={'container'}>
            <br/>
            <div className={`row`}>
                <div className={`col-md-6 offset-md-3`}>
                    <div className={`card`}>
                        <div className={`card-header`}>
                            <h2 className={`text-center`}>User Registeration</h2>
                        </div>
                        <div className={`card-body`}>
                            <form>
                                <div className={`row mb-3`}>
                                    <label className={`col-md-3 form-label`}>Username or Email: </label>
                                    <div className={`col-md-9`}>
                                        <input className={`form-control`}
                                               type={`text`}
                                               name={`username`}
                                               placeholder={`Enter username`}
                                               value={username}
                                               onChange={e => setUsername(e.target.value)}
                                        />
                                    </div>
                                </div>
                                <div className={`row mb-3`}>
                                    <label className={`col-md-3 form-label`}>Password: </label>
                                    <div className={`col-md-9`}>
                                        <input className={`form-control`}
                                               type={`password`}
                                               name={`password`}
                                               placeholder={`Enter password`}
                                               value={password}
                                               onChange={e => setPassword(e.target.value)}
                                        />
                                    </div>
                                </div>
                                <button className={`btn btn-primary col-md-3 offset-4`} type={`submit`}
                                        onClick={e => handleLogin(e)}>Login
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default LoginComponent;