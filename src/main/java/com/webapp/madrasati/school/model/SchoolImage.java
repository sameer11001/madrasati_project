// package com.webapp.madrasati.school.model;

// import com.webapp.madrasati.core.model.BaseEntity;

// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.FetchType;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.ManyToOne;
// import jakarta.persistence.Table;
// import lombok.AllArgsConstructor;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.Setter;

// @Entity
// @Table(name = "school_image")
// @AllArgsConstructor
// @NoArgsConstructor
// @Setter
// @Getter
// public class SchoolImage extends BaseEntity {

// @Column(name = "image_path")
// private String imagePath;

// @Column(name = "image_name")
// private String imageName;

// @ManyToOne(fetch = FetchType.LAZY)
// @JoinColumn(name = "school_id")
// private School school;

// }
