CREATE TABLE skills_jobs_ds_de_count_month AS (SELECT count(DISTINCT maindes.bgtjobid) as jobs,
count(DISTINCT skillsdes.skill) as skills, extract(month from maindes.jobdate) as mon,
 extract(year from maindes.jobdate) as yyyy, bgtoccname  
FROM maindes, skillsdes WHERE maindes.bgtjobid = skillsdes.bgtjobid 
//AND skillsdes.skillcluster NOT LIKE 'na'
GROUP by 5,4,3 order by 4,3,5 limit 4; );