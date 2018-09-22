This little tool is created to convert simple json files to csv so it can be edited in a spreadsheet program.
The files it can handle are simple files containing an array of objects that has attributes and subarrays containing objects with attributes.
The program first fetches the header. This in the form object.attributes. For the subarray this is subobject.attribute

Running the program with files/simple.json files/simple.csv
will convert simple.json file into simple.csv.
A check is done that the file is not already there.

Result
students.studentName;subjects.name;subjects.marks;students.Age;
Rob Noort;English;40;12;
;History;50;;
Barry Newman;English;40;12;
;History;50;;
;Science;40;;
Baz van de Veer;;empty array cant determine fields;12;


from 
{ "students" : [ {
        "studentName": "Rob Noort",
        "Age": "12",
        "subjects": [
            {
                "name": "English",
                "marks": "40"
            },
            {
                "name": "History",
                "marks": "50"
            }
        ]
    },
    {
        "studentName": "Barry Newman",
        "Age": "12",
        "subjects": [
            {
                "name": "English",
                "marks": "40"
            },
            {
                "name": "History",
                "marks": "50"
            },
            {
                "name": "Science",
                "marks": "40"
            }
        ]
    },
    {
        "studentName": "Baz van de Veer",
        "Age": "12",
        "subjects": []
    }]}
