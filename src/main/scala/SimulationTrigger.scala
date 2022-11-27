import helpers.CreateLogger
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicyRoundRobin
import org.cloudbus.cloudsim.schedulers.cloudlet.{CloudletSchedulerSpaceShared, CloudletSchedulerTimeShared}
import org.cloudbus.cloudsim.schedulers.vm.{VmSchedulerSpaceShared, VmSchedulerTimeShared}
import simulations.{AutoScaleSimulation, CloudModelsSimulation, RingModelSimulation, SchedulerSimulation, VmAllocPolRoundRobin}


// This is the entry point for triggering different simulation
object SimulationTrigger {
  val logger = CreateLogger(classOf[SimulationTrigger.type ])

  @main def trigger = {
    logger.info("Starting cloud models simulations...")

    // Cloud model simulation
    logger.info("Running cloud Simulation for Iaas Paas and Faas DC")
    val cloudModels = new CloudModelsSimulation()
    cloudModels.start()

    logger.info("cloud AutoScale Simulation")
    val autoscale = new AutoScaleSimulation()
    autoscale.start()

    logger.info("Running time scheduler simulation")
    val timeScheduler = new SchedulerSimulation("TimeShared", new VmSchedulerTimeShared(), new CloudletSchedulerTimeShared())
    timeScheduler.start()

    logger.info("Running space scheduler simulation")
    val spaceShared = new SchedulerSimulation("SpaceShared", new VmSchedulerSpaceShared(), new CloudletSchedulerSpaceShared())
    spaceShared.start()

    logger.info("Running VM Allocation simulation")
    val roundRobin = new VmAllocPolRoundRobin("RoundRobin", vmAllocation = new VmAllocationPolicyRoundRobin())
    roundRobin.start()

    logger.info("Running RingModel simulation")
    val ring = new RingModelSimulation()
    ring.start()
  }
}
