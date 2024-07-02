import React, {useEffect, useRef, useState} from "react";
import DefaultUser from '../../../data/users.json';
import HeaderComponent from "./HeaderComponent.tsx";
import {RenderGuests} from "./RenderGuests.tsx";

export interface UserType {
    name: string;
    age: number;
}

const UserSearch = (): React.ReactNode => {

    const [_name, _setName] = useState<string>('');
    const [_users, setUsers] = useState<UserType[]>([]);
    const [searchedUsers, setSearchedUsers] = useState<string[]>([]);

    const inputRef = useRef<HTMLInputElement | null>(null);

    useEffect(() => {
        inputRef.current?.focus()
        setUsers(DefaultUser as UserType[]);
    }, []);

    const onClickEventHandler = (event: React.MouseEvent<HTMLButtonElement>): void => {
        event.preventDefault();
        setSearchedUsers(_users.filter(user => user.name === _name).map(user => user.name));
    };

    const onCopyHandler = (_event: React.ClipboardEvent<HTMLInputElement>) => {
    };


    return (
        <React.Fragment>
            <HeaderComponent header={"User Search"}/>
            <form>
                <div className={"mb-3"}>
                    <div className={"row input-group"}>
                        <div className={"col-2"}>
                            <label htmlFor={"nameInput"} className={"input-group-text"}>Enter User Name</label>
                        </div>
                        <div className={"col-6"}>
                            <input value={_name}
                                   type={"text"}
                                   id={"nameInput"}
                                   ref={inputRef}
                                   className={"form-control col-6"}
                                   onCopy={onCopyHandler}
                                   onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
                                       setSearchedUsers([]);
                                       _setName(event.target.value);
                                   }
                                   }/>
                        </div>
                        <div className={"row m m-3"}>
                            <div className={"offset-2 col-2"}>
                                <button type={"submit"} className={"btn btn-success"} onClick={onClickEventHandler}>
                                    Search
                                </button>
                            </div>
                        </div>
                        <div className={"row m-3"}>
                            {_name !== '' && searchedUsers.length > 0 ? <RenderGuests guests={searchedUsers}/> :
                                <h2 className={"h2"}>No users with name {_name}</h2>}
                        </div>
                    </div>
                </div>
            </form>
        </React.Fragment>
    );
}

export default UserSearch;