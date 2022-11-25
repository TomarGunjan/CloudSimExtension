import com.typesafe.config.ConfigFactory
import helpers.{DCConfigHelper, ObtainConfigReference}
import org.cloudbus.cloudsim.brokers.{DatacenterBroker, DatacenterBrokerSimple}
import org.cloudbus.cloudsim.core.CloudSim
import org.scalatest.funspec.AnyFunSpec

class SimulationTests extends AnyFunSpec{

  val model = "RoundRobin"
  val config = ConfigFactory.load(model)

  describe("Obtain Config Parameters"){
    it("should contain cumber of hosts"){
      assert(config.getInt("datacenter.numOfHosts")==4)
    }

    it("should contain host ram"){
      assert(config.getInt("host.ram")==2048)
    }
  }

  describe("Test Utility function"){
    val cloudSim = new CloudSim()
    describe("cloud sim creation"){
      it("should be created"){
        assert(cloudSim!=null)
      }
    }

    val datacenterutil = new DCConfigHelper(cloudModel = model)
    val datacenter = datacenterutil.createDatacenter(cloudSim)

    describe("datacenter creation"){
      it("should be created"){
        assert(datacenter!=null)
      }
    }

    val broker = new DatacenterBrokerSimple(cloudSim)
    describe("broker creation"){
      it("should be created"){
        assert(broker!=null)
      }
    }

    val vmList = datacenterutil.createVms()
    describe("vmList creation"){
      it("should not be empty"){
        assert(vmList.length!=0)
      }
    }

    val cloudLetList = datacenterutil.createCloudlets()
    describe("create cloudlets"){
      it("should not be empty"){
        assert(cloudLetList.length!=0)
      }
    }

  }

}
