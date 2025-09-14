import { useState } from "react";

/**
 *
 * @param initalValue
 */
export const useToggle = (initalValue: boolean) => {
    const [value, setValue] = useState<boolean>(initalValue);

    const toggleValue = () => setValue((prev: boolean) => !prev);

    return {value, toggleValue};
}