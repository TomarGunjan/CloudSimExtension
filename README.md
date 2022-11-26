# CloudSimExtension

## Project Description

The intention of this project is to simulate cloud organisation using ClousSim plus framework. CloudSimplus is used to extend, enabling modeling, simulation, and experimentation of Cloud computing infrastructures and application services. It allows developers to focus on specific system design issues to be investigated, without concerning the low-level details related to Cloud-based infrastructures and services.

## About Cloudsim

In order to build a simulation scenario, we have to create at least:

1. a datacenter with a list of physical machines (Hosts);
2. a broker that allows submission of VMs and Cloudlets to be executed, on behalf of a given customer, into the cloud infrastructure;
3. a list of customer’s virtual machines (VMs);
4. and a list of customer’s cloudlets (objects that model resource requirements of different applications).

![image](https://user-images.githubusercontent.com/26132783/204076140-97afa929-de40-4cc0-bd9a-23ae7f748f3c.png)


### Some terminologies

1. Cloudlets - workloads that needs to be processed and executed
2. Broker - an entity that works on behalf of cloud user and submits vm request to datacenter

## About Project

### Entry class
1. SimulationTrigger.scala - This is entry point for running simulations

### Simulation Classes
These classes are responsible for simulation of different scenarios
1. CloudModelsSimulation.scala
2. SchedulerSimulation.scala
3. VmAllocPolRoundRobin.scala
4. AutoScaleSimulation.scala
5. RingModelSimulation.scala

### Helper
1. CreateLogger.scala - This class creates a logger instance
2. DCConfigHelper.scala - This clas provides utility functions for Simulation classes such as function for creating datacenters, creating hosts, fetching simulation configs

### ConfigManager
These classes initialise specific config for Simulations
1. GetCloudletConfig.scala
2. GetDCConfig.scala
3. GetHostConfig.scala
4. GetVMConfig.scala

### Tests
SimulationTests.scala

## Simulation Result and Inferences

### CloudModelsSimulation

This class is for simulating different cloud models including Iaas Paas and Saas and arranged in brite network topology
1. There are 3 data centers(representing Iaas, Paas, Saas) having 2 hosts, 1 VM and 2 cloudlets each.
3. The data centers and broker are connected in brite topology
4. The costs model for 3 datacenters are different and are mentioned in their respective configuration file with Iaas being most costly
   ### Saas
   Software as a Service provides with a completed product that is run and managed by the service provider. With a SaaS offering we do not have to think about how the    service is maintained or how the underlying infrastructure is managed. It provides least flexibility in terms on platform and Infrastructure customization and hence    is least expensive among the cloud models mentioned here.
   ### Paas
   Platforms as a service remove the need for organizations to manage the underlying infrastructure (usually hardware and operating systems) and allow us to focus on      the deployment and management of your applications. It provides more user control as compared to Saas hence it is more expensive than Saas
   ### Iaas
   Infrastructure as a Service provides with the highest level of flexibility and management control over the IT resources and hence its services are most expensive.
  ![image](https://user-images.githubusercontent.com/26132783/204107063-1b33415a-08dc-404b-9958-1e29ce8767bd.png)
5. The simulation results also concludes that Iaas is most expensive
  
  
 


### TimeSharedSchedulingSimulation

This class is for simulating Time shared VM and Cloudlet scheduling policies.
1. There is 1 host, 1 VM and 4 cloudlets
2. Cloudlet Sceduling Policy is Time Shared Sceduling Policy
![image](https://user-images.githubusercontent.com/26132783/204077863-abc14963-8402-452f-bb98-e52e35a9a171.png)

3. All the cloudlets were assigned to same host and VM
![image](https://user-images.githubusercontent.com/26132783/204077887-e2d930d8-8031-41e6-9c47-02af57abb399.png)
4. All the cloudlets took 160 seconds to run
5. The cloudlets costs are also same
![image](https://user-images.githubusercontent.com/26132783/204077845-5e27a446-2742-4eb2-a03d-d69fafcb4b84.png)


### SpaceSharedScedulingSimulation

This class is for simulating Space shared VM and Cloudlet scheduling policies.
![image](https://user-images.githubusercontent.com/26132783/204076617-15a86166-af60-4128-b539-031b78ff9167.png)

### RoundRobinVMAllocationPolicy

This class is for simulating Round Robin VM allocation policies
1. There are 4 hosts, 8 VMs and 8 cloudlets
2. The VM Allocation policy is RoundRobin
![image](https://user-images.githubusercontent.com/26132783/204077199-1df72984-b768-4fcc-bc04-aaa892c644e0.png)
3. Vms are allocated to hosts in a cyclic manner
![image](https://user-images.githubusercontent.com/26132783/204077115-a556b0b7-f4da-444d-a00c-642c7643631b.png)
2. RoundRobin is a simple but naive approach an may lead to resource wastage as it doesn't consider the nature of task.
![image](https://user-images.githubusercontent.com/26132783/204076642-60321302-5eab-46df-86c4-f0b1a9cbf83a.png)

### AutoScalingSimulation
 






