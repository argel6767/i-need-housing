"use client"


import React, { useState } from 'react';
import { SetUserTypeDto } from "@/interfaces/requests/userRequests";
import { updateUserType } from "@/endpoints/users";

const Card = () => {

    const [userType, setUserType] = useState<string>("");


    const handleUserType = (event: React.ChangeEvent<HTMLInputElement>) => {
        const type: string = event.target.value;
        setUserType(type.toUpperCase());
    }

    const saveUserType = async (e: React.MouseEvent | React.FormEvent) => {
      e.preventDefault(); // Add this line
      const email = sessionStorage.getItem('email')!;
      const requestBody: SetUserTypeDto = {
        email: email,
        userType: userType
      }
      await updateUserType(requestBody);
      //TODO handle navigation and failures
    }



  return (
    <form onSubmit={saveUserType} className="w-80 md:w-96 px-8 py-8 bg-slate-100 flex flex-col gap-5 rounded-lg shadow-lg motion-translate-x-in-[0%] motion-translate-y-in-[100%] motion-duration-1500">
      <legend className="text-2xl font-semibold pb-2">
        Who are you?
      </legend>
      <label  className={`text-lg h-16 relative hover:bg-slate-200 ${userType === 'INTERN' ? "bg-slate-200" : ""} transition-all duration-300 flex items-center px-5 gap-3 rounded-lg  select-none cursor-pointer`}>
        <span>Intern</span>
        <input onChange={handleUserType} type="radio" name="status" value={"Intern"} checked={userType === 'INTERN'} className="w-4 h-4 absolute  right-5 transition-all duration-300"  />
        <span className="absolute right-5 w-4 h-4 rounded-lg border-2  transition-all duration-300" />
      </label>
      <label  className={`text-lg h-16 relative hover:bg-slate-200 ${userType === 'NEW_GRAD' ? "bg-slate-200" : ""} transition-all duration-300 flex items-center px-5 gap-3 rounded-lg  select-none cursor-pointer`}>
        <span>New Grad</span>
        <input onChange={handleUserType} type="radio" name="status" value={"New_Grad"} checked={userType === 'NEW_GRAD'} className="w-4 h-4 absolute right-5 transition-all duration-300"  />
        <span className="absolute right-5 w-4 h-4 rounded-lg border-2 transition-all duration-300" />
      </label>
      <button type="submit" className="btn btn-outline hover:bg-slate-200 hover:text-black">Confirm</button>
    </form>
  );
}



const UserType = () => {
    return (
        <main className="flex flex-col gap-5 justify-center items-center text-center p-3 motion-translate-x-in-[0%] motion-translate-y-in-[100%]">
            <div className="flex justify-center gap-12">
                <Card/>
            </div>
        </main>
    )
}

export default UserType;