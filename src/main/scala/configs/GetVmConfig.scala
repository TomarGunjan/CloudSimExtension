package configs

import com.typesafe.config.ConfigFactory

/** This is a utility class for obtaining conmfig values for VM config key */
class GetVmConfig (cloudModel:String){
  val config = ConfigFactory.load(cloudModel)
  val mipsCapacity = config.getInt("vm.mipsCapacity")
  val ram = config.getInt("vm.ram")
  val bw = config.getInt("vm.bw")
  val size = config.getInt("vm.size")
  val numOfPes = config.getInt("vm.numOfPes")
}
