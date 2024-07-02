import React from "react";

interface RenderGuestsProps {
    guests: string[];
}

export const RenderGuests = (props: RenderGuestsProps): React.ReactNode => {

    return (
        <div className={"row"}>
            <ul>
                {props.guests.map((guest, index, _array) => <li key={index}>
                        {guest}
                    </li>
                )}
            </ul>
        </div>
    );
}