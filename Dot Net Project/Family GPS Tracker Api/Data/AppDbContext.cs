using System;
using Family_GPS_Tracker_Api.Domain;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata;
using static Family_GPS_Tracker_Api.Domain.IdentityModels;

#nullable disable

namespace Family_GPS_Tracker_Api.Domain
{
    public partial class AppDbContext : IdentityDbContext<ApplicationUser, ApplicationRole, Guid, ApplicationUserClaim, ApplicationUserRole, ApplicationUserLogin, ApplicationRoleClaim, ApplicationUserToken>
    {
        public AppDbContext()
        {
        }

        public AppDbContext(DbContextOptions<AppDbContext> options)
            : base(options)
        {
        }

        public virtual DbSet<Child> Children { get; set; }
        public virtual DbSet<Geofence> Geofences { get; set; }
        public virtual DbSet<Location> Locations { get; set; }
        public virtual DbSet<Notification> Notifications { get; set; }
        public virtual DbSet<Parent> Parents { get; set; }
        public virtual DbSet<RefreshToken> RefreshTokens { get; set; }
		public virtual DbSet<ApplicationUser> Users { get; set; }
		public virtual DbSet<ApplicationRole> Roles { get; set; }
		public virtual DbSet<ApplicationUserRole> UserRoles { get; set; }
        public virtual DbSet<PairingCode> PairingCodes { get; set; }


        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            if (!optionsBuilder.IsConfigured)
            {
                optionsBuilder.UseSqlServer("Name=MyDb");
            }
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);
            modelBuilder.Entity<Child>(entity =>
            {
                entity.ToTable("Child");
     
                entity.Property(e => e.ParentId).HasColumnName("parent_id");

                entity.HasOne(d => d.Parent)
                    .WithMany(p => p.Children)
                    .HasForeignKey(d => d.ParentId)
                    .HasConstraintName("FK_Child_Parent");

                entity.HasOne(e => e.User)
                .WithOne(d => d.Child)
                .HasForeignKey<Child>(f => f.ChildId)
                .HasConstraintName("FK_Child_User");
            });

            modelBuilder.Entity<PairingCode>(entity =>
            {
                entity.ToTable("PairingCode");
                entity.Property(e => e.PairingCodeId)
                .ValueGeneratedOnAdd();
                entity.HasOne(e => e.Child)
                .WithOne(c => c.PairingCode)
                .HasForeignKey<PairingCode>(p => p.ChildId)
                .HasConstraintName("FK_PairingCode_Child");

            });

            modelBuilder.Entity<ApplicationUserRole>(entity =>
            {

                entity.HasKey(sc => new { sc.UserId, sc.RoleId });

                entity.HasOne(ur => ur.User)
                .WithMany(u => u.UserRoles)
                .HasForeignKey(ur => ur.UserId)
                /*.OnDelete(DeleteBehavior.ClientSetNull)*/;

                entity.HasOne(ur => ur.Role)
                .WithMany(u => u.UserRoles)
                .HasForeignKey(ur => ur.RoleId);

            });

          


            modelBuilder.Entity<Geofence>(entity =>
            {
                entity.ToTable("Geofence");

                entity.Property(e => e.GeofenceId)
                    .ValueGeneratedNever()
                    .HasColumnName("geofence_id");

                entity.Property(e => e.Category)
                    .IsRequired()
                    .HasMaxLength(50)
                    .IsUnicode(false)
                    .HasColumnName("category");

                entity.Property(e => e.ChildId).HasColumnName("child_id");

                entity.Property(e => e.Latitude)
                    .HasColumnType("decimal(18, 0)")
                    .HasColumnName("latitude");

                entity.Property(e => e.Longitude)
                    .HasColumnType("decimal(18, 0)")
                    .HasColumnName("longitude");

                entity.Property(e => e.Radius).HasColumnName("radius");

                entity.HasOne(d => d.Child)
                    .WithMany(p => p.Geofences)
                    .HasForeignKey(d => d.ChildId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("FK_Geofence_Child");
            });

            modelBuilder.Entity<RefreshToken>(entity => {
                entity.ToTable("RefreshToken");
            });

            modelBuilder.Entity<Location>(entity =>
            {
                entity.ToTable("Location");

                entity.Property(e => e.LocationId)
                    .ValueGeneratedNever()
                    .HasColumnName("location_id");

                entity.Property(e => e.ChildId).HasColumnName("child_id");

                entity.Property(e => e.Latitude).HasColumnName("latitude");

                entity.Property(e => e.Longitude).HasColumnName("longitude");

                entity.Property(e => e.Time)
                    .IsRequired()
                    .HasMaxLength(80)
                    .IsUnicode(false)
                    .HasColumnName("time");

                entity.Property(e => e.UniqueNumber)
                    .ValueGeneratedOnAdd()
                    .HasColumnName("unique_number");

                entity.HasOne(d => d.Child)
                    .WithMany(p => p.Locations)
                    .HasForeignKey(d => d.ChildId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("FK_Location_Child");
            });

            modelBuilder.Entity<Notification>(entity =>
            {
                entity.ToTable("Notification");

                entity.Property(e => e.NotificationId)
                    .ValueGeneratedOnAdd()
                    .HasColumnName("notification_id");

                entity.Property(e => e.ChildId).HasColumnName("child_id");

                entity.Property(e => e.CreatedAt)
                    .IsRequired()
                    .HasMaxLength(50)
                    .IsUnicode(false)
                    .HasColumnName("created_at");

                entity.Property(e => e.Message)
                    .IsRequired()
                    .HasMaxLength(50)
                    .IsUnicode(false)
                    .HasColumnName("message");

                entity.Property(e => e.ParentId).HasColumnName("parent_id");

                entity.Property(e => e.Title)
                    .IsRequired()
                    .HasMaxLength(50)
                    .IsUnicode(false)
                    .HasColumnName("title");

                entity.HasOne(d => d.Child)
                    .WithMany(p => p.Notifications)
                    .HasForeignKey(d => d.ChildId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("FK_Notification_Child");

                entity.HasOne(d => d.Parent)
                    .WithMany(p => p.Notifications)
                    .HasForeignKey(d => d.ParentId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("FK_Notification_Parent");
            });

            modelBuilder.Entity<Parent>(entity =>
            {
                entity.ToTable("Parent");

                entity.HasKey(e => e.ParentId)
                    .HasName("parent_id");

                entity.Property(e => e.DeviceToken)
                    .HasMaxLength(220)
                    .IsUnicode(false)
                    .HasColumnName("device_token");

                entity.HasOne(e => e.User)
                .WithOne(d => d.Parent)
                .HasForeignKey<Parent>(f => f.ParentId)
                .HasConstraintName("FK_Parent_User");

                

                /* entity.Property(e => e.Email)
                     .IsRequired()
                     .HasMaxLength(50)
                     .IsUnicode(false)
                     .HasColumnName("email");

                 entity.Property(e => e.Name)
                     .IsRequired()
                     .HasMaxLength(50)
                     .IsUnicode(false)
                     .HasColumnName("name");

                 entity.Property(e => e.Password)
                     .IsRequired()
                     .HasMaxLength(50)
                     .IsUnicode(false)
                     .HasColumnName("password");

                 entity.Property(e => e.PhoneNumber)
                     .IsRequired()
                     .HasMaxLength(50)
                     .IsUnicode(false)
                     .HasColumnName("phone_number");*/
            });

		/*	modelBuilder.Entity<User>(entity =>
			{
				entity.ToTable("User");

				entity.Property(e => e.UserId)
					.ValueGeneratedNever()
					.HasColumnName("user_id");

				entity.Property(e => e.ChildId).HasColumnName("child_id");

				entity.Property(e => e.ParentId).HasColumnName("parent_id");

				entity.HasOne(d => d.Child)
					.WithMany(p => p.Users)

					.HasForeignKey(d => d.ChildId)
					.HasConstraintName("FK_User_Child");

				entity.HasOne(d => d.Parent)
					.WithMany(p => p.Users)
					.HasForeignKey(d => d.ParentId)
					.HasConstraintName("FK_User_Parent");

				
			});*/

            OnModelCreatingPartial(modelBuilder);
        }

        partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
    }
}
