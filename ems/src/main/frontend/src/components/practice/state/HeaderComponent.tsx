import React from "react";

interface HeaderProps {
    header: string;
}

const HeaderComponent = (props: HeaderProps): React.ReactNode => {

    return (
        <div className={"row mt-3 mb-3 text-center"}>
            <h1 className={"h1"}>{props.header}</h1>
        </div>
    )
}

export default HeaderComponent;