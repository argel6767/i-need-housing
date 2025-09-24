import { useState } from "react";

/**
 *
 * @param initialValue
 */
export const useToggle = (initialValue: boolean) => {
    const [value, setValue] = useState<boolean>(initialValue);

    const toggleValue = () => setValue((prev: boolean) => !prev);

    return {value, toggleValue};
}