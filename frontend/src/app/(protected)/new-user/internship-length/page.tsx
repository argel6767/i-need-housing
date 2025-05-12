'use client'

import { useGlobalContext } from "@/components/GlobalContext";
import { RawUserPreferenceDto } from "@/interfaces/requests/userPreferencesRequests";
import { useState } from "react";
import { PageTurner } from "../PageTurner";
import { SubmitUserInfoButton, UserInfoSubmissionForm } from "../UserPreferenceComponents";

interface InputProps {
    field: keyof RawUserPreferenceDto
}

const DatePicker = ({field}:InputProps ) => {
    // Initialize state with the initial value
    const {newUserInfo, setNewUserInfo} = useGlobalContext();
    const initialValue = field === 'startDate'? newUserInfo.newUserPreferencesDto.startDate :
        newUserInfo.newUserPreferencesDto.endDate;
    const [date, setDate] = useState<string>(initialValue || '2025-06-01');

    const handleDateChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setDate(event.target.value);
        setNewUserInfo(prev => ({
            userType: prev.userType,
            newUserPreferencesDto:{
                ...prev.newUserPreferencesDto,
                [field]:date}
        }))
        console.log(date);
    };

    const props = {
        type: "date",
        value:date,
        onChange: handleDateChange,
        ...(field === 'endDate' && {min: newUserInfo.newUserPreferencesDto.startDate} )
    }

    return (
        <input {...props} />
    );
};

const InternshipDatesForm = () => {

    const [isLoading, setIsLoading] = useState<boolean>(false);

    return (
        <main className="flex justify-center motion-translate-x-in-[0%] motion-translate-y-in-[100%] motion-duration-1500">
            <UserInfoSubmissionForm setIsLoading={setIsLoading}>
                    <article className="flex flex-col justify-center h-full p-4 gap-7">
                        <legend className="text-2xl font-semibold pb-2">When is your internship?</legend>
                        <div className="flex flex-col gap-1">
                            <label className="text-lg shadow-md">Internship Start Date</label>
                            <DatePicker  field="startDate"/>
                        </div>
                        <div className="flex flex-col gap-1">
                            <label className="text-lg shadow-md">Internship End Date</label>
                            <DatePicker  field="endDate"/>
                        </div>
                    </article>
                    <nav className="flex justify-between mt-auto pt-4 items-center">
                    <PageTurner href="/new-user/bed-bath/" direction="left"/>
                    <SubmitUserInfoButton isLoading={isLoading}/>
                </nav>
            </UserInfoSubmissionForm>
        </main>
    )
}

export default InternshipDatesForm