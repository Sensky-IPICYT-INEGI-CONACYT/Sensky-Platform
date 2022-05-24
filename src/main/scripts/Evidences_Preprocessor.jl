using JSON, Mongoc, CSV, CSVFiles, LightXML, DataFrames, DataStructures;
using Dates;

function getStartTimestamp()
    descriptor = JSON.parsefile("/home/ubuntu/SenSky_Parallel/parallel_descriptor.json")
    return descriptor["evidences-preprocessor"]["start-timestamp"]
end

function updateStartTimestamp( timestamp )
    descriptor = JSON.parsefile("/home/ubuntu/SenSky_Parallel/parallel_descriptor.json")
    descriptor["evidences-preprocessor"]["start-timestamp"] = timestamp
    open("/home/ubuntu/SenSky_Parallel/parallel_descriptor.json","w") do f
        JSON.print(f, descriptor)
    end
end

function addRow(df, row)
    println("adding rows!: ", row)
    finalRow = Dict()
    for name in names(df)
        if haskey(row, name)
            finalRow[name] = row[name]
            delete!(row, name)
        else
            finalRow[name] = "NaN"
        end
    end
    for missingKey in keys(row)
        insertcols!(df, missingKey => "NaN")
        finalRow[missingKey] = row[missingKey]
    end
    push!(df, finalRow)
    return df
end

function getAnswers(survey, document)
    Survey= OrderedDict(
    "filePath" => document["survey-path"],
    "latitude" => string(document["latitude"]),
    "longitude" => string(document["longitude"]),
    "workshopKey" => document["workshop-key"],
    "surveyKey" => document["survey-key"],
    "capturedTimestamp" => string(document["captured-timestamp"])
    )
    indexTmp = 1
    for topic in get_elements_by_tagname(survey, "topic")
        
        for answer in get_elements_by_tagname(topic, "question")
            println("procesando Q$(parse(Int, attribute(answer, "id")))")
            push!(Survey, "Q$(parse(Int, attribute(answer, "id")))" => content(find_element(answer, "answer")) )
            indexTmp = indexTmp + 1
        end
    end
    return Survey
end



function main()
    # Input Arguments
    from = getStartTimestamp()
    to = datetime2unix(Dates.now()) * 1000
    output = "/var/www/html/sensky_store/surveys_resume.csv"
    evidencesPath = "/var/www/html/sensky_store/"

    #DB Connection
    evidences = Mongoc.Client("localhost", 27017)["SenSkyDEV2"]["evidences"]
    criteria = string("{\"stored-timestamp\":{\"\$gt\": ", from, ", \"\$lte\": " , to, "}, \"survey-key\": {\"\$exists\":true}}")
    occurrences = Mongoc.find(evidences, Mongoc.BSON(criteria))


    if !isfile(output)
       data = DataFrame(
        filePath = [],
        latitude = [],
        longitude = [],
        workshopKey = [],
        surveyKey = [],
        capturedTimestamp = []
        )
    else
        data=string.(DataFrame(load(output; delim=';')))
    end

    for document in occurrences
        doc = string(evidencesPath, document["survey-path"])
        survey = root(parse_file("$doc"))
        if size(data)==(0, 0) || !(document in data[:,1])
            addRow(data, getAnswers(survey, document))
        end
    end
    CSV.write(output, data; delim=';')
    updateStartTimestamp(to)
    println("Evidences CSV updated")
    sleep(10*60)
end

while true
    println("[Evidences Preprocessor] Starting")
    task = @async main()
    wait(task)
end



