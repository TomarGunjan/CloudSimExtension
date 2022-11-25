package helpers

import com.typesafe.config.{Config, ConfigFactory}
import configs.{GetCloudletConfig, GetDCConfig, GetHostConfig, GetVmConfig}
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicySimple
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple
import org.cloudbus.cloudsim.cloudlets.CloudletSimple
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudbus.cloudsim.datacenters.{Datacenter, DatacenterSimple}
import org.cloudbus.cloudsim.hosts.HostSimple
import org.cloudbus.cloudsim.resources.{Pe, PeSimple, Processor}
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelDynamic
import org.cloudbus.cloudsim.vms.{Vm, VmSimple}
import org.cloudsimplus.builders.tables.CloudletsTableBuilder
import org.cloudbus.cloudsim.schedulers.cloudlet.{CloudletSchedulerAbstract, CloudletSchedulerSpaceShared, CloudletSchedulerTimeShared}
import org.cloudbus.cloudsim.schedulers.vm
import org.cloudbus.cloudsim.schedulers.vm.{VmScheduler, VmSchedulerAbstract, VmSchedulerSpaceShared, VmSchedulerTimeShared}
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletScheduler
import org.cloudbus.cloudsim.provisioners.ResourceProvisionerSimple
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicy
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelFull
import org.cloudsimplus.autoscaling.{VerticalVmScaling, VerticalVmScalingSimple}

import scala.collection.JavaConverters.*

/**
 * This is a utility class for fetching config and providing helper functions
 * @param cloudModel
 * @param vmScheduler
 * @param cloudletScheduler
 * @param vmAllocation
 */
class DCConfigHelper(cloudModel : String, vmScheduler: VmScheduler = new VmSchedulerSpaceShared(), cloudletScheduler: CloudletScheduler = new CloudletSchedulerTimeShared(), vmAllocation: VmAllocationPolicy = new VmAllocationPolicySimple()) {
  val logger = CreateLogger(classOf[DCConfigHelper.type])

  // Configuration of datacenter
  val datacenterConfig = new GetDCConfig(cloudModel)

  // Configuration of hosts in a datacenter
  val hostConfig = new GetHostConfig(cloudModel)

  // Configuration of VMs to be created
  val vmConfig = new GetVmConfig(cloudModel)

  // Configuration of cloudlets to be assigned to VMs to be created
  val cloudletConfig = new GetCloudletConfig(cloudModel)

  logger.info(s"Configuration parsing completed from $cloudModel.conf.")

  val numberOfHosts = datacenterConfig.numberOfHosts
  val numOfVms = datacenterConfig.numOfVms
  val numofCloudlets = datacenterConfig.numOfCloudlets
  
  logger.info(s"Number of hosts: $numberOfHosts")
  logger.info(s"Number of VMs: $numOfVms")
  logger.info(s"Number of cloudlets: $numofCloudlets")

    
   // Creates a datacenter with configured hosts, VM Allocation Policy and other configs specified in config file
  def createDatacenter(cloudsim: CloudSim): Datacenter = {
    val hostList = createHost(datacenterConfig.numberOfHosts)
    val dc = new DatacenterSimple(cloudsim, hostList.asJava, vmAllocation)
    dc.getCharacteristics().setArchitecture(datacenterConfig.arch).setOs(datacenterConfig.os).setCostPerBw(datacenterConfig.costPerBw).setCostPerStorage(datacenterConfig.costPerStorage).setCostPerMem(datacenterConfig.costPerMem)
    return dc
  }
    
   //Creates a list of hosts with  pe, VM Scheduling policy, RAM, Bandwidth and storage and other configs
  def createHost(numberOfHosts: Int) = {
    val peList: List[Pe] = 1.to(hostConfig.numberOfPes).map(x => new PeSimple(hostConfig.mipsCapacity)).toList
    1.to(numberOfHosts).map(x => new HostSimple(hostConfig.ram, hostConfig.bw, hostConfig.storage, peList.asJava, true).setVmScheduler(vmScheduler.getClass().getDeclaredConstructor().newInstance())).toList
  }


 
   //Creates a list of VMs with configured MIPS Capacity, PEs, RAM, Bandwidth requested.
  def createVms() = {
    val numOfVms = datacenterConfig.numOfVms
    1.to(numOfVms).map(x =>
      new VmSimple(vmConfig.mipsCapacity, vmConfig.numOfPes, cloudletScheduler.getClass().getDeclaredConstructor().newInstance()).setRam(vmConfig.ram).setBw(vmConfig.bw).setSize(vmConfig.size)
    ).toList
  }

 
   //Creates a list of cloudlets with with configured Utilization model, length, PEs requested.
  def createCloudlets() = {
    val numOfCloudlets = datacenterConfig.numOfCloudlets
    val utilizationModel = cloudletConfig.utilizationModel
    1.to(numOfCloudlets).map(x => new CloudletSimple(cloudletConfig.length, cloudletConfig.pesNumber, utilizationModel).setSizes(cloudletConfig.size)).toList
  }

  //Create cloudlets with different delays
  def createCloudletsWithDifferentDelays = {
    val numOfCloudlets = datacenterConfig.numOfCloudlets
    val cloudlets = numOfCloudlets * 1.5.round.toInt
    1.to(cloudlets).map(x => new CloudletSimple(cloudletConfig.length*x, cloudletConfig.pesNumber, x*2).setSizes(cloudletConfig.size)).toList
  }

  //Create VMS with scalable PEs
  def createScalableVms() = {
    val numOfVms = datacenterConfig.numOfVms
    1.to(numOfVms).map(x =>
      new VmSimple(vmConfig.mipsCapacity, vmConfig.numOfPes, cloudletScheduler.getClass().getDeclaredConstructor().newInstance()).setRam(vmConfig.ram).setBw(vmConfig.bw).setSize(vmConfig.size).setPeVerticalScaling(createVerticalPeScaling)
    ).toList
  }

  // Create PE scaling which can be scaled up and down
  private def createVerticalPeScaling: VerticalVmScaling = { 
    //The percentage in which the number of PEs has to be scaled
    val scalingFactor = 0.1
    val verticalCpuScaling = new VerticalVmScalingSimple(classOf[Processor], scalingFactor)

    val multiplier = 2
    verticalCpuScaling.setResourceScaling((vs: VerticalVmScaling) => multiplier * vs.getScalingFactor * vs.getAllocatedResource)
    verticalCpuScaling.setLowerThresholdFunction(this.lowerCpuUtilizationThreshold)
    verticalCpuScaling.setUpperThresholdFunction(this.upperCpuUtilizationThreshold)
    verticalCpuScaling
  }

  private def lowerCpuUtilizationThreshold(vm: Vm) = 0.4

  private def upperCpuUtilizationThreshold(vm: Vm) = 0.8

}
