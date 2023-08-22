package by.btslogistics.autorelease.service.releasedocument.steps;

import by.btslogistics.autorelease.service.dto.RegistrationDto;

public abstract class RegistrationDocument {

    private RegistrationDocument next;

    public RegistrationDocument linkWith(RegistrationDocument next) {
        this.next = next;
        return next;
    }

    public abstract RegistrationDto doStep(RegistrationDto  dto);

    protected RegistrationDto nextStep(RegistrationDto dto){
        if(next == null){
            return dto;
        }
        return next.doStep(dto);

    }

}
