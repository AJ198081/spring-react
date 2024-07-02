import React, {useState} from "react";
import {RenderGuests} from "./RenderGuests.tsx";
import AddGuests from "./AddGuests.tsx";
import HeaderComponent from "./HeaderComponent.tsx";

const GuestList = () => {

    const [guests, setGuests] = useState<string[]>([]);

    return (
        <React.Fragment>
            <HeaderComponent header={"Guests Management"}/>
            <AddGuests guests={guests} addGuests={setGuests}/>
            <RenderGuests guests={guests}/>
        </React.Fragment>
    );
}

export default GuestList;