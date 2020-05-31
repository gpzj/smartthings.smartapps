
/**
 *  Bathroom Fan On When Humidity Quickly Rises (And shut off on the decline)
 *
 *  Author: Justin Wildeboer
 */

definition(
    name: "humidity-on-the-rise",
    namespace: "gpzj",
    author: "Justin Wildeboer",
    description: "Bathroom Fan On When Humidity Quickly Rises (And shut off on the decline)",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Meta/light_presence-outlet.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Meta/light_presence-outlet@2x.png"
)

preferences {
  section("Humidity Sensor") {
    input "humiditySensor", "capability.relativeHumidityMeasurement", multiple: true, title: "Choose Humidity Sensor"
  }
  section("Bathroom Fan") {
    input "bathroomFan", "capability.switch", title: "Choose Bathroom Fan Switch"
  }
  section("Alerting") {
     input "phoneNum", "phone", title: "SMS Phone Number.", required: false, multiple: true
  }
     input "sendPush", "bool", required: false,
              title: "Send Push Notification?"
  }
}

def installed() {
  log.debug "${app.label} installed with settings: ${settings}"
  initialize()
}

def updated() {
  log.debug "${app.label} updated with settings: ${settings}"
  initialize()
}

def uninstall() {
  log.debug "${app.label} uninstalled"
  unsubscribe()
}

def initialize() {
    subscribe(humiditySensor, "humidity", humidityChangeCheck)
    subscribe(bathroomFan, "switch", humidityChangeCheck)
}

def humidityChangeCheck(evt) {
  log.debug "handler $evt.name: $evt.value"
  def message = "$evt.name: $evt.value $humiditySensor $humiditySensor.humidity"
    if (sendPush) {
        sendPush(message)
    }
    if (phoneNum) {
        sendSms(phoneNum, message)
    }
}
