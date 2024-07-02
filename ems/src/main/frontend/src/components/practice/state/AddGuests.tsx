import React, {useState} from "react";

interface AddGuestsProps {
    guests: string[];
    addGuests: React.Dispatch<React.SetStateAction<string[]>>;
}

const AddGuests = (props: AddGuestsProps): React.ReactNode => {
    const [guestName, setGuestName] = useState<string>('');


    const onChangeEventHandler = ((event: React.ChangeEvent<HTMLInputElement>) => {
        setGuestName(event.target.value)
    });

    const onCopyHandler = (event: React.ClipboardEvent<HTMLInputElement>) => {
        event.preventDefault();
        console.log("hello");
        console.log(event.clipboardData);
        console.log(event);

    };
    return (
        <div className={"row"}>
            <form>
                <div className={"row mb-3"}>
                    <div className={"col col-2"}>
                        <label htmlFor="guestName" className={"form-label h5"}>Enter Guest's Name </label>
                    </div>
                    <div className={"col col-6"}>
                        <input type={"text"} id="guestName" className={"form-control"} value={guestName}
                               onChange={onChangeEventHandler} onCopy={onCopyHandler} onCopyCapture={onCopyHandler}/>
                    </div>
                </div>
                <div className={"row"}>
                    <div className={"col offset-2 col-4"}>
                        <button type={"submit"} className={"btn btn-info"}
                                onClick={(event: React.MouseEvent<HTMLButtonElement>) => {
                                    event.preventDefault();
                                    setGuestName('')
                                    props.addGuests(prevState => [guestName, ...prevState]);
                                }
                                }>Submit
                        </button>
                    </div>
                </div>
            </form>
        </div>

    );
}

export default AddGuests;