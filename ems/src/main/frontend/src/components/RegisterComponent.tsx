import React, {useState} from "react";
import {registerUserDetails} from "../services/AuthorisationService.ts";
import {RegisterationDetails} from "../types/RegisterationDetails.ts";

const RegisterComponent = (): React.ReactNode => {

    const [name, setName]: [string, React.Dispatch<React.SetStateAction<string>>] = useState<string>('')
    const [username, setUsername]: [string, React.Dispatch<React.SetStateAction<string>>] = useState<string>('')
    const [password, setPassword]: [string, React.Dispatch<React.SetStateAction<string>>] = useState<string>('')
    const [email, setEmail]: [string, React.Dispatch<React.SetStateAction<string>>] = useState<string>('')
    const [roles, setRoles]: [string[], (value: (((prevState: string[]) => string[]) | string[])) => void] = useState<string[]>([])

    const rolesHandler = (e: React.ChangeEvent<HTMLInputElement>) => {
        const rolesArray = e.target.value.split(`,`).map(role => role.trim());
        setRoles(rolesArray);
    };

    const handleRegistration = (e: React.MouseEvent<HTMLButtonElement>) => {
        e.preventDefault();

        const userDetails  = {name, username, password, email, roles} as RegisterationDetails

        (async () => {
            try {
                const response = await registerUserDetails(userDetails);
                if (response.status === 200) {
                    console.log(response.data);
                }
            } catch (error) {
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
                                    <label className={`col-md-3 form-label`}>Name: </label>
                                    <div className={`col-md-9`}>
                                        <input className={`form-control`}
                                               type={`text`}
                                               name={`name`}
                                               placeholder={`Enter name`}
                                               value={name}
                                               onChange={e => setName(e.target.value)}
                                        />
                                    </div>
                                </div>
                                <div className={`row mb-3`}>
                                    <label className={`col-md-3 form-label`}>Username: </label>
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
                                <div className={`row mb-3`}>
                                    <label className={`col-md-3 form-label`}>Email: </label>
                                    <div className={`col-md-9`}>
                                        <input className={`form-control`}
                                               type={`text`}
                                               name={`email`}
                                               placeholder={`Enter email`}
                                               value={email}
                                               onChange={e => setEmail(e.target.value)}
                                        />
                                    </div>
                                </div>
                                <div className={`row mb-3`}>
                                    <label className={`col-md-3 form-label`}>Roles: </label>
                                    <div className={`col-md-9`}>
                                        <input className={`form-control`}
                                               type={`text`}
                                               name={`roles`}
                                               placeholder={`Enter roles, comma separated`}
                                               value={roles}
                                               onChange={e => rolesHandler(e)}
                                        />
                                    </div>
                                </div>
                                <button className={`btn btn-primary col-md-3 offset-4`} type={`submit`}
                                        onClick={e => handleRegistration(e)}>Submit
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default RegisterComponent;