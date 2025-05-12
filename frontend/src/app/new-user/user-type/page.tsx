"use client"


import React, { useState } from 'react';
import { SetUserTypeDto } from "@/interfaces/requests/userRequests";
import { updateUserType } from "@/endpoints/users";
import { Loader } from 'lucide-react';
import { useRouter } from 'next/navigation';
import { sleep } from '@/app/utils/utils';
import { useGlobalContext } from '@/components/GlobalContext';
import { PageTurner } from '../PageTurner';

const UserTypeForm = () => {
    
    const {setIsIntern, setNewUserInfo, newUserInfo} = useGlobalContext();
    const [userType, setUserType] = useState<string>("");


    const handleUserType = (event: React.ChangeEvent<HTMLInputElement>) => {
        const type: string = event.target.value;
        setUserType(type);
        setNewUserInfo(prev => ({
          userType: type,
          newUserPreferencesDto: {...prev.newUserPreferencesDto}
        }))
        type === 'INTERN'? setIsIntern(true) : setIsIntern(false);
    }

  return (
    <form className="w-80 md:w-96 px-8 py-8 bg-slate-100 flex flex-col gap-5 rounded-lg shadow-lg motion-translate-x-in-[0%] motion-translate-y-in-[100%] motion-duration-1500">
      <legend className="text-2xl font-semibold pb-2">Who are you?</legend>
      <label  className={`text-lg h-16 relative hover:bg-slate-200 ${userType === 'INTERN' ? "bg-slate-200" : ""} transition-all duration-300 flex items-center px-5 gap-3 rounded-lg  select-none cursor-pointer`}>
        <span>Intern</span>
        <input onChange={handleUserType} type="radio" name="status" value={"INTERN"} checked={userType === 'INTERN' || newUserInfo.userType === 'INTERN'} className="w-4 h-4 absolute  right-5 transition-all duration-300"  />
        <span className="absolute right-5 w-4 h-4 rounded-lg border-2  transition-all duration-300" />
      </label>
      <label  className={`text-lg h-16 relative hover:bg-slate-200 ${userType === 'NEW_GRAD' ? "bg-slate-200" : ""} transition-all duration-300 flex items-center px-5 gap-3 rounded-lg  select-none cursor-pointer`}>
        <span>New Grad</span>
        <input onChange={handleUserType} type="radio" name="status" value={"NEW_GRAD"} checked={userType === 'NEW_GRAD' || newUserInfo.userType === 'NEW_GRAD'} className="w-4 h-4 absolute right-5 transition-all duration-300"  />
        <span className="absolute right-5 w-4 h-4 rounded-lg border-2 transition-all duration-300" />
      </label>
      <nav className='flex justify-end mt-auto pt-4'>
        <PageTurner href='/new-user/location' direction='right'/>
      </nav>
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