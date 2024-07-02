import {Child} from "./Child.tsx";

export const Parent = () => {
    return <Child  color={"red"} onClick={() => console.log("Clicked")}>
        <h1>This is a child</h1>
    </Child>
}