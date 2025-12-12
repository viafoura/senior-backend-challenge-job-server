package com.example.jobserver.application.port.in;

public interface SubmitJob {
    JobResponse submit(SubmitJobCommand command);
}
