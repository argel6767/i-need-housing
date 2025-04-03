"use client"


import React, { useState } from 'react';
import { SetUserTypeDto } from "@/interfaces/requests/userRequests";
import { updateUserType } from "@/endpoints/users";
import { Loader } from 'lucide-react';
import { useRouter } from 'next/navigation';
import { sleep } from '@/app/utils/utils';
import { useGlobalContext } from '@/components/GlobalContext';

const UserTypeForm = () => {
    
    const router = useRouter();
    const {setIsIntern} = useGlobalContext();
    const [userType, setUserType] = useState<string>("");
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const [isError, setIsError] = useState<boolean>(false);


    const handleUserType = (event: React.ChangeEvent<HTMLInputElement>) => {
        const type: string = event.target.value;
        setUserType(type);
        type === 'INTERN'? setIsIntern(true) : setIsIntern(false);
    }

    const saveUserType = async (e: React.MouseEvent | React.FormEvent) => {
      e.preventDefault(); // Add this line
      setIsLoading(true);
      const email = sessionStorage.getItem('email')!;
      const requestBody: SetUserTypeDto = {
        email: email,
        userType: userType
      }
      const data = await updateUserType(requestBody);
      if (!data) {
        setIsLoading(false);
        setIsError(true)
        await sleep(1500);
        setIsError(false);
      }
      else {
        setIsLoading(false);
        router.push("/new-user/location")
      }
    }



  return (
    <form onSubmit={saveUserType} className="w-80 md:w-96 px-8 py-8 bg-slate-100 flex flex-col gap-5 rounded-lg shadow-lg motion-translate-x-in-[0%] motion-translate-y-in-[100%] motion-duration-1500">
      <legend className="text-2xl font-semibold pb-2">
        Who are you?
      </legend>
      <label  className={`text-lg h-16 relative hover:bg-slate-200 ${userType === 'INTERN' ? "bg-slate-200" : ""} transition-all duration-300 flex items-center px-5 gap-3 rounded-lg  select-none cursor-pointer`}>
        <span>Intern</span>
        <input onChange={handleUserType} type="radio" name="status" value={"INTERN"} checked={userType === 'INTERN'} className="w-4 h-4 absolute  right-5 transition-all duration-300"  />
        <span className="absolute right-5 w-4 h-4 rounded-lg border-2  transition-all duration-300" />
      </label>
      <label  className={`text-lg h-16 relative hover:bg-slate-200 ${userType === 'NEW_GRAD' ? "bg-slate-200" : ""} transition-all duration-300 flex items-center px-5 gap-3 rounded-lg  select-none cursor-pointer`}>
        <span>New Grad</span>
        <input onChange={handleUserType} type="radio" name="status" value={"NEW_GRAD"} checked={userType === 'NEW_GRAD'} className="w-4 h-4 absolute right-5 transition-all duration-300"  />
        <span className="absolute right-5 w-4 h-4 rounded-lg border-2 transition-all duration-300" />
      </label>
      <label className={`text-red-500 font-bold ${isError? "":"hidden"}`}>Something went wrong! Try again.</label>
      <button disabled={userType === ''} type="submit" className="btn btn-outline hover:bg-slate-200 hover:text-black">Confirm<Loader size={22} className={`animate-pulse ${isLoading ? "" : "hidden"}`}/></button>
    </form>
  );
}



const UserType = () => {
    return (
        <main className="flex flex-col gap-5 justify-center items-center text-center p-3 motion-translate-x-in-[0%] motion-translate-y-in-[100%]">
            <div className="flex justify-center gap-12">
                <UserTypeForm />
            </div>
        </main>
    )
}

export default UserType;