import HTTP, JSON, Mongoc
using Dates

# sensors = ["13781", "13779"]
sensors = ["13061", "13774", "13779", "13781", "14049", "15204", "15746"]
token = "IlFkezrcU2Jvs5fm9oDLhSbn"

function getStartTimestamp()
    descriptor = JSON.parsefile("parallel_descriptor.json")
    return descriptor["plume-pollutants-collector"]["start-timestamp"]
end

function updateStartTimestamp( timestamp )
    descriptor = JSON.parsefile("parallel_descriptor.json")
    descriptor["plume-pollutants-collector"]["start-timestamp"] = timestamp
    open("parallel_descriptor.json","w") do f
        JSON.print(f, descriptor)
    end
end

function constructDB(fromDateTime, collection)

    # Database connection
    db = Mongoc.Client("localhost", 27017)["YouiLabDataLayers"][collection]

    while fromDateTime < Dates.now(UTC)
        for sensor in sensors
            println("[PlumeLabs] - starting with sensor: ", sensor)
            timeWindow = [
                trunc(Int64, Dates.datetime2unix(fromDateTime)),
                trunc(Int64, Dates.datetime2unix(fromDateTime + Dates.Day(1)))
            ]
            println("[PlumeLabs] - processing time window: ", timeWindow)
            httpRequest = string("https://api.plumelabs.com/2.0/organizations/",
                "41/sensors/measures?sensor_id=", sensor,
                "&token=", token,"&start_date=", timeWindow[1],
                "&end_date=", timeWindow[2])
            answer = JSON.parse(String(HTTP.get(httpRequest).body))

            println("[PlumeLabs] - insertando: ", answer["total"])
            # println(answer)


            if answer["total"] > 0
                for measure in answer["measures"]
                    if measure != nothing
                        criteria = Dict(
                            "sensor-id" => sensor,
                            "timestamp" => measure["date"]
                        )
                        entry = merge(criteria, Dict("pollutants" => measure["pollutants"]))
                        reply = Mongoc.find_and_modify(
                            db,
                            Mongoc.BSON(criteria),
                            update=Mongoc.BSON(entry),
                            flags=Mongoc.FIND_AND_MODIFY_FLAG_UPSERT | Mongoc.FIND_AND_MODIFY_FLAG_RETURN_NEW
                        )
                    end
                end
            end
        end
        fromDateTime = fromDateTime + Dates.Day(1)
    end
    updateStartTimestamp(datetime2unix(fromDateTime - Dates.Day(1)))
end

function main()
    fromDateTime = Dates.unix2datetime(getStartTimestamp())
    println("[PlumeLabs Pollutants Collector] - from DateTime => ", fromDateTime)
    constructDB(fromDateTime, "PlumePollutants")
    sleep(10*60)
end

while true
    println("[PlumeLabs Pollutants Collector] Starting")
    task = @async main()
    wait(task)
end


