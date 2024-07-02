import React, {MouseEventHandler, PropsWithChildren} from "react";

interface ChildProps extends PropsWithChildren{
    onClick: MouseEventHandler<HTMLButtonElement>; //TODO: Do Not type it as 'void' function, will work but not descriptive
    color: string;
}

export const Child = (props: ChildProps): React.ReactNode => {
    return <div>
        {props.color}
        <button onClick={props.onClick}>Click Me!!</button>
    </div>
}